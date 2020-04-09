import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MenuSelectionTabsComponent } from './menu-selection-tabs.component';

describe('MenuSelectionTabsComponent', () => {
  let component: MenuSelectionTabsComponent;
  let fixture: ComponentFixture<MenuSelectionTabsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MenuSelectionTabsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MenuSelectionTabsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
