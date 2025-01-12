import type {Executor} from '../';
import type {LoginInput, SessionView} from '../model/static/';

export class Session {
    
    constructor(private executor: Executor) {}
    
    readonly login: (options: SessionOptions['login']) => Promise<
        SessionView
    > = async(options) => {
        let _uri = '/session';
        return (await this.executor({uri: _uri, method: 'POST', body: options.body})) as Promise<SessionView>;
    }
    
    readonly logout: () => Promise<
        void
    > = async() => {
        let _uri = '/session';
        return (await this.executor({uri: _uri, method: 'DELETE'})) as Promise<void>;
    }
}

export type SessionOptions = {
    'login': {
        body: LoginInput
    }, 
    'logout': {}
}
