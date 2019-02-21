/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

/**
 * Service which handles authorization for incoming requests.
 */
package io.cellery.security.cell.sts.server.authorization;

import io.cellery.security.cell.sts.server.authorization.opa.OPAAuthorizationHandler;
import io.cellery.security.cell.sts.server.core.model.CellStsRequest;
import io.cellery.security.cell.sts.server.core.model.config.CellStsConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cell sts authorization service.
 */
public class AuthorizationService {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationService.class);

    AuthorizationHandler authorizationHandler = new OPAAuthorizationHandler();

    /**
     * @param request
     * @param jwt
     * @throws AuthorizationFailedException
     */
    public void authorize(CellStsRequest request, String jwt) throws AuthorizationFailedException {

        if (!CellStsConfiguration.getInstance().isAuthorizationEnabled()) {
            log.debug("Authorization is disabled. Hence returning without evaluating policies");
            return;
        }
        log.info("Authorization service is invoked for request: {}", request.getRequestId());
        authorizationHandler.authorize(buildAuthorizeRequest(request, jwt));

    }

    private AuthorizeRequest buildAuthorizeRequest(CellStsRequest request, String jwt) throws
            AuthorizationFailedException {

        log.info("Building authorize request with jwt: " + jwt);
        AuthorizationContext authorizationContext = new AuthorizationContext(jwt);
        AuthorizeRequest authorizeRequest = new AuthorizeRequest(request, authorizationContext);
        return authorizeRequest;
    }
}
