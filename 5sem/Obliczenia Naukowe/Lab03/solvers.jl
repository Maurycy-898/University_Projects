# autor: Maurycy Sosnowski (261705)

module Solvers export bisect, newton, secant

    #========================================================================================================= 
        Funkcja rozwiązująca równanie f(x) = 0 metodą bisekcji.
        Dane:
            f – funkcja f(x) zadana jako anonimowa funkcja,
            a, b – końce przedziału początkowego,
            delta, epsilon – dokładności obliczeń.
        Wyniki:
            Czwórka (r, v, it, err), gdzie
            r – przybliżenie pierwiastka równania f(x) = 0,
            v – wartość f(r),
            it – liczba wykonanych iteracji,
            err – sygnalizacja błędu:
                0 - brak błędu,
                1 - funkcja nie zmienia znaku w przedziale [a,b] 
    ==========================================================================================================#
    function bisect(f::Function, a::Float64, b::Float64, delta::Float64, epsilon::Float64)
        u = f(a) # wyliczamy wartość funkcji dla "a"
        v = f(b) # wyliczmy wartość funkcji dla "b"

        # zwracamy brak rozwiązań (Nothing) w przypadku gdy wartości funkcji na końcach przedziału mają ten sam znak
        if (sign(u) == sign(v)) 
            return (Nothing, Nothing, 0, 1)  # (w tym przypadku znacznik błędu err = 1) 
        end

        e = b - a # długość przedziału
        i = 0     # do liczenia iteracji
        while true
            i = i + 1 # aktualizacja ilości iteracji
            e = e / 2 # aktualizacja długości przedziału (dzielimy na "pół")
            c = a + e # wyznaczenie środka przedziału
            w = f(c)  # wyliczenie wartości funkcji w środku przedziału

            # Warunek zakończenia obliczeń:
            # kończymy gdy długość przedziału "e" < zadana "delta"
            # lub gdy osiągniemy zadaną dokładność "epsilon"
            if (abs(e) < delta || abs(w) < epsilon)
                return (c, w, i, 0) # zwracamy wyniki
            end
            
            if (sign(w) != sign(u))
                # Jeżeli znak przy wartości funkcji w środku przedziału jest różny od tej w punkcie "a" (taki sam jak w "b"):
                # aktualizujemy wartości "b" i "f(b)" dla nowego przedziału:
                b = c # "c" zostaje nowym "b"
                v = w # "f(b) aktualizujemy na "f(c)"
            else
                # Jeżeli znak przy wartości funkcji w środku przedziału jest taki jak w punkcie "a":
                # aktualizujemy wartości "a" i "f(a)" dla nowego przedziału:
                a = c # "c" zostaje nowym "a"
                u = w # "f(a) aktualizujemy na "f(c)"
            end
        end
    end


    #=========================================================================================================    
        Funkcja rozwiązująca równanie f(x) = 0 metodą Newtona.
        Dane:
            f, pf – funkcja f(x) oraz pochodna f'(x) zadane jako anonimowe funkcje,
            x0 – przybliżenie początkowe,
            delta, epsilon – dokładności obliczeń,
            maxit – maksymalna dopuszczalna liczba iteracji.
        Wyniki:
            Czwórka (r, v, it, err), gdzie
            r – przybliżenie pierwiastka równania f(x) = 0,
            v – wartość f(r),
            it – liczba wykonanych iteracji,
            err – sygnalizacja błędu:
                0 - brak błędu,
                1 - nie osiągnięto wymaganej dokładności w maxit iteracji,
                2 - pochodna bliska zeru.
    ==========================================================================================================#
    function newton(f::Function, fPrim::Function, x0::Float64, delta::Float64, epsilon::Float64, maxIt::Int)
        v = f(x0) # wyliczamy wartość funkcji dla "x0" - początkowego przybliżenia

        # kończymy jeśli wartość funkcji w "x0", jest bliska zeru z dokładnością do zadanego "epsilon"
        if (abs(v) < epsilon) 
            return (x0, v, 0, 0)
        end

        # kończymy jeśli wartość pochodnej funkcji w "x0", jest bliska zeru z dokładnością do zadanego "epsilon"
        if (abs(fPrim(x0)) < epsilon)
            return (x0, v, 0, 2) # zwracamy wyniki, (sygnalizator błędu = 2)
        end

        for i in (1:maxIt)
            x1 = x0 - (v / fPrim(x0)) # obliczmy wartość "x1" - kolejnego przybliżenia
            v = f(x1) # obliczamy wartość funkcji w "x1"

            # Warunek zakończenia obliczeń:
            # odległość między kolejnymi przybliżeniami - "x0", "x1" < zadana "delta"
            # lub gdy osiągniemy zadaną dokładność "epsilon"
            if (abs(x1 - x0) < delta || abs(v) < epsilon)
                return (x1, v, i, 0)
            end
            
            x0 = x1 # aktualizujemy przybliżenie początkowe "x0" 
        end

        # Gdy nie osiągnieto wymaganej dokładności "epsilon" w zadanej ilości "maxIt" iteracji
        return (x1, v, maxIt, 1) # zwracamy wyniki, (sygnalizator błędu = 1)
    end


    #=========================================================================================================
        Funkcja rozwiązująca równanie f(x) = 0 metodą siecznych.
        Dane:
            f – funkcja f(x) zadana jako anonimowa funkcja,
            x0, x1 – przybliżenia początkowe,
            delta, epsilon – dokładności obliczeń,
            maxit – maksymalna dopuszczalna liczba iteracji.
        Wyniki:
            Czwórka (r, v, it, err), gdzie
            r – przybliżenie pierwiastka równania f(x) = 0,
            v – wartość f(r),
            it – liczba wykonanych iteracji,
            err – sygnalizacja błędu:
                0 - brak błędu,
                1 - nie osiągnięto wymaganej dokładności w maxit iteracji.
    ==========================================================================================================#
    function secant(f::Function, x0::Float64, x1::Float64, delta::Float64, epsilon::Float64, maxIt::Int)
        fx0 = f(x0) # obliczamy wartość funkcji w przybliżeniu początkowym "x0"
        fx1 = f(x1) # obliczamy wartość funkcji w przybliżeniu początkowym "x1"

        for i in (1:maxIt)
            if (abs(fx0) < abs(fx1)) # Jeśli "f(x0)" < "f(x1)":
                x0, x1 = x1, x0        # zamieniamy "x0" z "x1"
                fx0, fx1 = fx1, fx0    # zamieniamy "f(x0)" z "f(x1)"
            end

            s = (x1 - x0) / (fx1 - fx0)
            x0 = x1              # aktualizujemy "x0" (= "x1")
            fx0 = fx1            # aktualizujemy "fx0" (= f(x1)")
            x1 = x1 - (fx1 * s)  # następna wartość "x1" w punkcie przecięcia stycznej z osią OX
            fx1 = f(x1)          # aktualizujemy wartość funkcji dla nowego "x1" 

            # Warunek zakończenia obliczeń:
            # odległość między kolejnymi przybliżeniami - "x0", "x1" < zadana "delta"
            # lub gdy osiągniemy zadaną dokładność "epsilon"
            if (abs(x1 - x0) < delta || abs(fx1) < epsilon)
                return (x1, fx1, i, 0)
            end
        end

        # Gdy nie osiągnieto wymaganej dokładności "epsilon" w zadanej ilości "maxIt" iteracji
        return (x1, fx1, maxIt, 1) # zwracamy wyniki, (sygnalizator błędu = 1)
    end

end
