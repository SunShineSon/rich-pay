package com.service.sms.util;


public abstract class NeteaseRestAPI implements RestAPI {

	private ClientContext context;

	private RestAPIInvoker invoker;

	public abstract String getResourceRootURI(String uri);

	public abstract String getResourceRootURL();

	public abstract String getResourceRootPREFIX();

	public abstract String getIMUserNamePREFIX();

	public ClientContext getContext() {
		return context;
	}

	public void setContext(ClientContext context) {
		this.context = context;
	}

	public RestAPIInvoker getInvoker() {
		return invoker;
	}

	public void setInvoker(RestAPIInvoker invoker) {
		this.invoker = invoker;
	}

}
