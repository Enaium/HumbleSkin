export type AllErrors = {
        family: 'ACCOUNT', 
        code: 'EMAIL_EXIST'
    } | {
        family: 'ACCOUNT', 
        code: 'CONFIRM_PASSWORD_ERROR'
    } | {
        family: 'SESSION', 
        code: 'USERNAME_OR_PASSWORD_ERROR'
    };
export type ApiErrors = {
    'account': {
        'register': AllErrors & ({
                family: 'ACCOUNT', 
                code: 'EMAIL_EXIST', 
                readonly [key:string]: any
            } | {
                family: 'ACCOUNT', 
                code: 'CONFIRM_PASSWORD_ERROR', 
                readonly [key:string]: any
            })
    }, 
    'character': {
    }, 
    'session': {
        'login': AllErrors & ({
                family: 'SESSION', 
                code: 'USERNAME_OR_PASSWORD_ERROR', 
                readonly [key:string]: any
            })
    }, 
    'texture': {
    }
};
