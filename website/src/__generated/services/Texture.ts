import type {Executor} from '../';
import type {TextureInput} from '../model/static/';

export class Texture {
    
    constructor(private executor: Executor) {}
    
    readonly save: (options: TextureOptions['save']) => Promise<
        void
    > = async(options) => {
        let _uri = '/texture';
        return (await this.executor({uri: _uri, method: 'PUT', body: options.body})) as Promise<void>;
    }
}

export type TextureOptions = {
    'save': {
        body: TextureInput
    }
}
