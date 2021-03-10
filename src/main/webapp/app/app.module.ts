import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import './vendor';
import { LacampesinaSharedModule } from 'app/shared/shared.module';
import { LacampesinaCoreModule } from 'app/core/core.module';
import { LacampesinaAppRoutingModule } from './app-routing.module';
import { LacampesinaHomeModule } from './home/home.module';
import { LacampesinaEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';


@NgModule({
  imports: [
    BrowserModule,
    LacampesinaSharedModule,
    LacampesinaCoreModule,
    LacampesinaHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    LacampesinaEntityModule,
    LacampesinaAppRoutingModule,
    BrowserAnimationsModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent],
})
export class LacampesinaAppModule {}
