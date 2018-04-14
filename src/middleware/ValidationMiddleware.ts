import * as Koa from 'koa';

import * as validate from 'validate.js';

export default (shape: object) => {
    return async (ctx: Koa.Context, next: () => Promise<any>) => {
        try {
            await validate.async(ctx.request.body, shape);
        } catch (e) {
            let err = { reason: e };
            throw err;
        }

        await next();
    }
}