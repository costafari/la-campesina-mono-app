import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { EventEmitterService } from '../entities/EventEmitterService';

// PrimeNG Imports
import { TableModule } from 'primeng/table';
import { PanelModule } from 'primeng/panel';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { InputMaskModule } from 'primeng/inputmask';
import { ToastModule } from 'primeng/toast';
import { ToolbarModule } from 'primeng/toolbar';
import { RippleModule } from 'primeng/ripple';
import { DialogModule } from 'primeng/dialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { InputNumberModule } from 'primeng/inputnumber';

// Servicios Prime
import { ConfirmationService, MessageService } from 'primeng/api';

@NgModule({
  exports: [
    FormsModule,
    CommonModule,
    NgbModule,
    InfiniteScrollModule,
    FontAwesomeModule,
    ReactiveFormsModule,
    TableModule,
    PanelModule,
    InputTextModule,
    ButtonModule,
    MenuModule,
    DropdownModule,
    InputTextareaModule,
    InputMaskModule,
    ToastModule,
    ToolbarModule,
    RippleModule,
    DialogModule,
    ConfirmDialogModule,
    InputNumberModule,
  ],
  providers: [EventEmitterService, ConfirmationService, MessageService],
})
export class SharedLibsModule {}
