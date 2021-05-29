import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AtualizaLocalizacaoComponent } from './atualiza-localizacao.component';

describe('AtualizaLocalizacaoComponent', () => {
  let component: AtualizaLocalizacaoComponent;
  let fixture: ComponentFixture<AtualizaLocalizacaoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AtualizaLocalizacaoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AtualizaLocalizacaoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
