import type {Executor} from '../';
import type {AccountView, RegisterInput} from '../model/static/';

export class Account {
    
    constructor(private executor: Executor) {}
    
    readonly get: () => Promise<
        AccountView
    > = async() => {
        let _uri = '/account';
        return (await this.executor({uri: _uri, method: 'GET'})) as Promise<AccountView>;
    }
    
    readonly register: (options: AccountOptions['register']) => Promise<
        void
    > = async(options) => {
        let _uri = '/account/register';
        return (await this.executor({uri: _uri, method: 'POST', body: options.body})) as Promise<void>;
    }
}

export type AccountOptions = {
    'register': {
        body: RegisterInput
    }, 
    'get': {}
}
