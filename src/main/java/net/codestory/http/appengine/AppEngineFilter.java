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
import com.google.apphosting.runtime.timer.Timer;
import com.google.apphosting.vmruntime.*;
import net.codestory.http.Context;
import net.codestory.http.filters.Filter;
import net.codestory.http.filters.PayloadSupplier;
import net.codestory.http.payload.Payload;

import java.io.IOException;

public class AppEngineFilter implements Filter {
    private final VmMetadataCache metadataCache;
    private final Timer wallclockTimer;

    public AppEngineFilter() {
        this.metadataCache = new VmMetadataCache();
        this.wallclockTimer = new VmTimer();
        ApiProxy.setDelegate(new VmApiProxyDelegate());
    }

    @Override
    public boolean matches(String uri, Context context) {
        return !uri.startsWith("/webjars/");
    }

    @Override
    public Payload apply(String uri, Context context, PayloadSupplier nextFilter) throws IOException {
        if (uri.equals("/_ah/start") || uri.equals("/_ah/stop") || uri.equals("/_ah/health")) {
            return new Payload("ok");
        }

        ApiProxy.setEnvironmentForCurrentThread(
                new LazyApiProxyEnvironment(() -> VmApiProxyEnvironment.createFromHeaders(
                        System.getenv(),
                        metadataCache,
                        name -> context.request().header(name),
                        VmRuntimeUtils.getApiServerAddress(),
                        wallclockTimer,
                        VmRuntimeUtils.ONE_DAY_IN_MILLIS
                )));

        return nextFilter.get();
    }
}
