type ServerConfig = {
    host: string;
}

export class Config {
    static fromJson(): Config { 
        const conf = require('./config.json');
        return conf as Config;
    }

    constructor() {}

    server: ServerConfig;
}

export default Config.fromJson();