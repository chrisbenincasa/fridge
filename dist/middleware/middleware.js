"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
class Middleware {
    static setupMiddleware(app) {
        // Is this the best (right) way? 
        app.use(new LoggingMiddleware().onRequest);
        app.use(new TimingMiddleware().onRequest);
    }
}
exports.Middleware = Middleware;
class BaseMiddleware {
}
exports.BaseMiddleware = BaseMiddleware;
class LoggingMiddleware extends BaseMiddleware {
    async onRequest(ctx, next) {
        console.log(`requesting url = ${ctx.url}`);
        await next();
    }
}
exports.LoggingMiddleware = LoggingMiddleware;
class TimingMiddleware extends BaseMiddleware {
    async onRequest(ctx, next) {
        console.time('request');
        await next();
        console.timeEnd('request');
    }
}
exports.TimingMiddleware = TimingMiddleware;
//# sourceMappingURL=middleware.js.map