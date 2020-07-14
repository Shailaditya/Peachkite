import {AppComponent} from './app.component';
import {Ng2StateDeclaration} from '@uirouter/angular';

/** The top level state(s) */
export let APP_STATES: Ng2StateDeclaration[] = [
    // The top-level app state.
    // This state fills the root <ui-view></ui-view> (defined in index.html) with the AppComponent
    {name: 'app',url: '/peachkite',component: AppComponent}
];