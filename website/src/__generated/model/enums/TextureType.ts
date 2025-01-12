export const TextureType_CONSTANTS = [
    'SKIN', 
    'CAPE', 
    'ELYTRA'
] as const;
export type TextureType = typeof TextureType_CONSTANTS[number];
