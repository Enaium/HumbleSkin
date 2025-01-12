export const ModelType_CONSTANTS = [
    'STEVE', 
    'ALEX'
] as const;
export type ModelType = typeof ModelType_CONSTANTS[number];
