package com.zhongjian.servlet.cvstore;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhongjian.dto.cart.basket.query.CartBasketDelQueryDTO;
import com.zhongjian.dto.common.CommonMessageEnum;
import com.zhongjian.dto.common.ResultUtil;
import org.apache.log4j.Logger;

import com.zhongjian.common.FormDataUtil;
import com.zhongjian.common.GsonUtil;
import com.zhongjian.common.ResponseHandle;
import com.zhongjian.common.SpringContextHolder;
import com.zhongjian.executor.ThreadPoolExecutorSingle;
import com.zhongjian.service.cart.cvstore.CartCvstoreService;

import java.io.IOException;
import java.util.Map;

@WebServlet(value = "/v1/cvcart/delete", asyncSupported = true)
public class DeleteCartOneServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(DeleteCartOneServlet.class);

	private CartCvstoreService cartCvstoreService = (CartCvstoreService) SpringContextHolder.getBean(CartCvstoreService.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Map<String, String> formData = FormDataUtil.getFormData(request);
		AsyncContext asyncContext = request.startAsync();
		ServletInputStream inputStream = request.getInputStream();
		
		inputStream.setReadListener(new ReadListener() {
			@Override
			public void onDataAvailable() throws IOException {
			}

			@Override
			public void onAllDataRead() {
				ThreadPoolExecutorSingle.executor.execute(() -> {
					String result = GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.SERVERERR));
					try {
					Integer uid = (Integer) request.getAttribute("uid");
					Integer basketId = Integer.valueOf(formData.get("id"));
					result = DeleteCartOneServlet.this.handle(uid, basketId);
					// 返回数据
				
						ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
					} catch (Exception e) {
						try {
							ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						log.error("fail cvcart/delete : " + e.getMessage());
					}
					asyncContext.complete();
				});
			}

			@Override
			public void onError(Throwable t) {
				asyncContext.complete();
			}
		});

	}

	private String handle(Integer uid, Integer id) {
		if (uid == 0) {
			return GsonUtil.GsonString(ResultUtil.getFail(CommonMessageEnum.USER_IS_NULL));
		}
		CartBasketDelQueryDTO cartBasketDelQueryDTO = new CartBasketDelQueryDTO();
		cartBasketDelQueryDTO.setUid(uid);
		cartBasketDelQueryDTO.setId(id);
		return GsonUtil.GsonString(cartCvstoreService.deleteInfoById(cartBasketDelQueryDTO));
	}
}