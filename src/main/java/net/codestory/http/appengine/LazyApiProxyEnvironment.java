/**
 * Copyright (C) 2013 all@code-story.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package net.codestory.http.appengine;

import com.google.apphosting.api.ApiProxy;

import java.util.Map;
import java.util.function.Supplier;

class LazyApiProxyEnvironment implements ApiProxy.Environment {
    private final Supplier<ApiProxy.Environment> supplier;

    private ApiProxy.Environment delegate;

    LazyApiProxyEnvironment(Supplier<ApiProxy.Environment> supplier) {
        this.supplier = supplier;
    }

    private ApiProxy.Environment delegate() {
        if (delegate == null) {
            delegate = supplier.get();
        }
        return delegate;
    }

    @Override
    public String getAppId() {
        return delegate().getAppId();
    }

    @Override
    public String getModuleId() {
        return delegate().getModuleId();
    }

    @Override
    public String getVersionId() {
        return delegate().getVersionId();
    }

    @Override
    public String getEmail() {
        return delegate().getEmail();
    }

    @Override
    public boolean isLoggedIn() {
        return delegate().isLoggedIn();
    }

    @Override
    public boolean isAdmin() {
        return delegate().isAdmin();
    }

    @Override
    public String getAuthDomain() {
        return delegate().getAuthDomain();
    }

    @Override
    @Deprecated
    public String getRequestNamespace() {
        return delegate().getRequestNamespace();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegate().getAttributes();
    }

    @Override
    public long getRemainingMillis() {
        return delegate().getRemainingMillis();
    }
}
