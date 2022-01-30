package de.dhbw.mosbach.compilerbau;
import java.util.HashSet;
import java.util.Set;

/*
        * Author:    Lukas
        * Created:   28.01.2022
        *
*/

public abstract class SyntaxNode {
    public Boolean nullable;
    public final Set<Integer> firstpos = new HashSet<>();
    public final Set<Integer> lastpos = new HashSet<>();
}

