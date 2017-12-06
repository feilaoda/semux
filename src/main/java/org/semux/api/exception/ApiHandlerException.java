/**
 * Copyright (c) 2017 The Semux Developers
 *
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.semux.api.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

public class ApiHandlerException extends Exception {

    private static final long serialVersionUID = -493145471711360873L;

    public String response;

    public HttpResponseStatus status;

    public ApiHandlerException(String response, HttpResponseStatus status) {
        this.response = response;
        this.status = status;
    }

    public ApiHandlerException(HttpResponseStatus status) {
        this.response = status.toString();
        this.status = status;
    }
}
