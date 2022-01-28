package de.dhbw.mosbach.compilerbau;

interface Visitable{
  void accept(Visitor visitor);
}
