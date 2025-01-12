import type {ModelType} from '../enums/';

export interface CharacterInput {
    name: string;
    model: ModelType;
    accountId: string;
}
