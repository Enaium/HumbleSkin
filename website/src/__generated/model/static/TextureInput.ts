import type {TextureType} from '../enums/';

export interface TextureInput {
    characterId: string;
    type: TextureType;
    content: string;
}
