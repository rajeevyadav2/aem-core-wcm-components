/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2016 Adobe Systems Incorporated
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
package com.adobe.cq.wcm.core.components.commons;

import javax.servlet.ServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;

import com.day.cq.wcm.api.AuthoringUIMode;
import com.day.cq.wcm.api.components.Component;
import com.day.cq.wcm.commons.WCMUtils;

public final class AuthoringUtils {

    /**
     * Checks if the components rendered in the passed request will be rendered for the Touch UI editor.
     *
     * @param request the request for which to check the editor type
     * @return {@code true} if the editor for the current request belongs to Touch UI, {@code false} otherwise
     */
    public static boolean isTouch(ServletRequest request) {
        AuthoringUIMode currentMode = AuthoringUIMode.fromRequest(request);
        return AuthoringUIMode.TOUCH == currentMode;
    }

    /**
     * Checks if the components rendered in the passed request will be rendered for the Classic UI editor.
     *
     * @param request the request for which to check the editor type
     * @return {@code true} if the editor for the current request belongs to Classic UI, {@code false} otherwise
     */
    public static boolean isClassic(ServletRequest request) {
        AuthoringUIMode currentMode = AuthoringUIMode.fromRequest(request);
        return AuthoringUIMode.CLASSIC == currentMode;
    }

    /**
     * Get the current component title form the resource
     *
     * @param resource the current resource
     * @param defaultTitle the default if component is null or title is empty
     * @return {@code String} of component tile or defaultTitle
     */
    public static String getComponentTitle(Resource resource, String defaultTitle) {
        String title = StringUtils.defaultString(defaultTitle, StringUtils.EMPTY);
        Component component = WCMUtils.getComponent(resource);
        if(component != null) {
            title = StringUtils.defaultIfEmpty(component.getTitle(), title) ;
        }
        return title;
    }
}
