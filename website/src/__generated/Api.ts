import type {Executor} from './';
import {
    Account, 
    Character, 
    Session, 
    Texture
} from './services/';

export class Api {
    
    readonly account: Account
    
    readonly character: Character
    
    readonly session: Session
    
    readonly texture: Texture
    
    constructor(executor: Executor) {
        this.account = new Account(executor);
        this.character = new Character(executor);
        this.session = new Session(executor);
        this.texture = new Texture(executor);
    }
}