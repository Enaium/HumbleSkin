import type {Executor} from '../';
import type {CharacterInput} from '../model/static/';

export class Character {
    
    constructor(private executor: Executor) {}
    
    readonly save: (options: CharacterOptions['save']) => Promise<
        void
    > = async(options) => {
        let _uri = '/character';
        return (await this.executor({uri: _uri, method: 'PUT', body: options.body})) as Promise<void>;
    }
}

export type CharacterOptions = {
    'save': {
        body: CharacterInput
    }
}
