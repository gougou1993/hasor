/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hasor.web.binder;
import net.hasor.core.ApiBinder;
import net.hasor.core.BindInfo;
import net.hasor.core.binder.ApiBinderCreater;
import net.hasor.web.ServletVersion;
import net.hasor.web.WebApiBinder;
import net.hasor.web.encoding.EncodingFilter;

import javax.servlet.ServletContext;
/**
 * @version : 2016-12-16
 * @author 赵永春 (zyc@hasor.net)
 */
public class WebApiBinderCreater implements ApiBinderCreater {
    @Override
    public Object createBinder(final ApiBinder apiBinder) {
        //
        Object context = apiBinder.getEnvironment().getContext();
        if (!(context instanceof ServletContext)) {
            return null;
        }
        //
        ServletContext servletContext = (ServletContext) context;
        ManagedServletPipeline sPipline = new ManagedServletPipeline();
        ManagedFilterPipeline fPipline = new ManagedFilterPipeline(sPipline);
        ManagedListenerPipeline lPipline = new ManagedListenerPipeline();
        //
        apiBinder.bindType(ManagedServletPipeline.class).toInstance(sPipline);
        apiBinder.bindType(FilterPipeline.class).toInstance(fPipline);
        apiBinder.bindType(ListenerPipeline.class).toInstance(lPipline);
        //
        ServletVersion curVersion = ServletVersion.V2_3;
        try {
            apiBinder.getEnvironment().getClassLoader().loadClass("javax.servlet.ServletRequestListener");
            curVersion = ServletVersion.V2_4;
            servletContext.getContextPath();
            curVersion = ServletVersion.V2_5;
            servletContext.getEffectiveMajorVersion();
            curVersion = ServletVersion.V3_0;
            servletContext.getVirtualServerName();
            curVersion = ServletVersion.V3_1;
        } catch (Throwable e) { /* 忽略 */ }
        //
        /*绑定ServletContext对象的Provider*/
        apiBinder.bindType(ServletContext.class).toInstance(servletContext);
        /*绑定当前Servlet支持的版本*/
        apiBinder.bindType(ServletVersion.class).toInstance(curVersion);
        /*请求响应编码*/
        BindInfo<EncodingFilter> bindInfo = apiBinder.bindType(EncodingFilter.class)//
                .toInstance(new EncodingFilter())//
                .toInfo();
        //
        WebApiBinder webApiBinder = new InnerWebApiBinder(curVersion, apiBinder);
        webApiBinder.filter("/*").through(0, bindInfo);
        return webApiBinder;
    }
}