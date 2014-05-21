
/*
 *    Copyright 1996-2014 UOL Inc
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.github.dmutti.driver.object.validator;

import java.math.*;
import java.util.*;
import com.github.dmutti.annotations.arrays.*;
import com.github.dmutti.annotations.collections.*;
import com.github.dmutti.annotations.general.*;
import com.github.dmutti.annotations.maps.*;
import com.github.dmutti.annotations.numbers.*;
import com.github.dmutti.annotations.strings.*;
import com.github.dmutti.object.validator.*;

interface ITeste {

    @StringIsAlphanumeric
    @StringIsNumeric
    String iTeste = null;
}

class Membro {

    @StringNotEmpty
    private String y;

    @NotNull
    private Membro membro;
}

@SuppressWarnings("rawtypes")
class Teste {
    @CollectionLength(min=1)
    protected List<String> l;

    @NumberNotNegative
    protected double i = -0.00000000001D;

    @MapNotEmpty
    protected Map m/* = Collections.singletonMap(null, null)*/;
}

class TesteFilho extends Teste implements ITeste {

    public TesteFilho() {
        l = Arrays.asList(new String[] {"x"});
    }

    @StringRegex("\\d*")
    private String x = "123456789";

    @StringLength(min=0, max=2)
    private String s = "000";

    @StringNotEmpty
    private static String staticString;

    @NotNull
    protected Object membro;

    @NotNull
    private BigDecimal dec;

    @ArrayLength
    String[] array;
}

public class ValidationDriver {

    public static void main(String[] args) {

        Validator validator = new Validator();
        TesteFilho filho = new TesteFilho();
        Membro membro = new Membro();
        filho.membro = membro;
        try {
            validator.validate(filho);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
