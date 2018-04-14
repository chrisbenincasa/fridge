import * as Koa from 'koa';

export class Middleware {
    static setupMiddleware(app: Koa): void {
        // Is this the best (right) way? 
        app.use(new LoggingMiddleware().onRequest);
        app.use(new TimingMiddleware().onRequest);
    }
}

export abstract class BaseMiddleware {
    abstract async onRequest(ctx: Koa.Context, next: () => Promise<any>): Promise<void>
}

export class LoggingMiddleware extends BaseMiddleware {
    async onRequest(ctx: Koa.Context, next: () => Promise<any>): Promise<void> {
        console.log(`requesting url = ${ctx.url}`);
        await next();
    }
}

export class TimingMiddleware extends BaseMiddleware {
    async onRequest(ctx: Koa.Context, next: () => Promise<any>): Promise<void> {
        console.time('request');
        await next();
        console.timeEnd('request');
    }
}