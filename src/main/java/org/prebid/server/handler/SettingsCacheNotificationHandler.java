package org.prebid.server.handler;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.prebid.server.settings.CacheNotificationListener;
import org.prebid.server.settings.proto.request.InvalidateSettingsCacheRequest;
import org.prebid.server.settings.proto.request.UpdateSettingsCacheRequest;

import java.util.Objects;

/**
 * Handles HTTP requests for updating/invalidating settings cache.
 */
public class SettingsCacheNotificationHandler implements Handler<RoutingContext> {

    private CacheNotificationListener cacheNotificationListener;

    public SettingsCacheNotificationHandler(CacheNotificationListener cacheNotificationListener) {
        this.cacheNotificationListener = Objects.requireNonNull(cacheNotificationListener);
    }

    @Override
    public void handle(RoutingContext context) {
        switch (context.request().method()) {
            case POST:
                doSave(context);
                break;
            case DELETE:
                doInvalidate(context);
                break;
            default:
                doFail(context);
        }
    }

    /**
     * Propagates updating settings cache
     */
    private void doSave(RoutingContext context) {
        final Buffer body = context.getBody();
        if (body == null) {
            respondWith(context, HttpResponseStatus.BAD_REQUEST, "Missing update data.");
            return;
        }

        final UpdateSettingsCacheRequest request;
        try {
            request = Json.decodeValue(body, UpdateSettingsCacheRequest.class);
        } catch (DecodeException e) {
            respondWith(context, HttpResponseStatus.BAD_REQUEST, "Invalid update.");
            return;
        }

        cacheNotificationListener.save(request.getRequests(), request.getImps());
        respondWith(context, HttpResponseStatus.OK, null);
    }

    /**
     * Propagates invalidating settings cache
     */
    private void doInvalidate(RoutingContext context) {
        final Buffer body = context.getBody();
        if (body == null) {
            respondWith(context, HttpResponseStatus.BAD_REQUEST, "Missing invalidation data.");
            return;
        }

        final InvalidateSettingsCacheRequest request;
        try {
            request = Json.decodeValue(body, InvalidateSettingsCacheRequest.class);
        } catch (DecodeException e) {
            respondWith(context, HttpResponseStatus.BAD_REQUEST, "Invalid invalidation.");
            return;
        }

        cacheNotificationListener.invalidate(request.getRequests(), request.getImps());
        respondWith(context, HttpResponseStatus.OK, null);
    }

    /**
     * Makes failure response in case of unexpected request
     */
    private void doFail(RoutingContext context) {
        respondWith(context, HttpResponseStatus.METHOD_NOT_ALLOWED, null);
    }

    /**
     * Sends HTTP response according to the given status and body
     */
    private static void respondWith(RoutingContext context, HttpResponseStatus status, String body) {
        final HttpServerResponse response = context.response().setStatusCode(status.code());
        if (body != null) {
            response.end(body);
        } else {
            response.end();
        }
    }
}
