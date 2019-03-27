package com.zhongjian.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.zhongjian.common.GsonUtil;
import com.zhongjian.common.ResponseHandle;
import com.zhongjian.common.SpringContextHolder;
import com.zhongjian.common.Status;
import com.zhongjian.executor.ThreadPoolExecutorSingle;
import com.zhongjian.service.TestService;
import java.io.IOException;
import java.util.HashMap;

@WebServlet(value = "/test", asyncSupported = true)
public class HomePageServlet extends HttpServlet {

	private static Logger log = Logger.getLogger(HomePageServlet.class);

	private TestService testService = (TestService) SpringContextHolder.getBean(TestService.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		AsyncContext asyncContext = request.startAsync();
		ServletInputStream inputStream = request.getInputStream();
		inputStream.setReadListener(new ReadListener() {

			@Override
			public void onDataAvailable() throws IOException {
			}

			@Override
			public void onAllDataRead() {
				ThreadPoolExecutorSingle.executor.execute(() -> {
					HashMap<String, Object> respData = null;
					String result = null;
					try {
						String numberStr = asyncContext.getRequest().getParameter("number");
						// 耗时操作
						HashMap<String, Object> resultMap = new HashMap<>();
						resultMap.put("output", testService.testMethod(numberStr));
						respData = new HashMap<>();
						respData.put("data", resultMap);
						respData.put("code", Status.Success.getStatenum());
						result = GsonUtil.GsonString(respData);
					} catch (Exception e) {
						respData = new HashMap<>();
						respData.put("code", Status.GeneralError.getStatenum());
						respData.put("msg","");
						result = GsonUtil.GsonString(respData);
					}
					try {
						ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
					} catch (IOException e) {
						log.error("fail sevlet3.1: " + e.getMessage());
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

}