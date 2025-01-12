import type {AccountView_TargetOf_characters} from './';

export interface AccountView {
    id: string;
    email: string;
    characters: Array<AccountView_TargetOf_characters>;
}
