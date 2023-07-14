#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

bool match(char* wzorzec, char* lancuch){
    
    int dlugosc_wzorzec = strlen(wzorzec);
    int dlugosc_lancuch = strlen(lancuch);
    
    if(dlugosc_wzorzec>0){                                                              //Pierwszy przypadek

        int j=0;                                                            //j wskazuje aktualną litere łańcucha
        for(int i=0; i<dlugosc_wzorzec && j<dlugosc_lancuch; i++){         //i wskazuje aktualną literę wzorca
            
            if(wzorzec[i]=='*'){
                while(wzorzec[i]=='*' && i < dlugosc_wzorzec){      //gdy i-ta litera to gwiazdka, szukamy najbliższej litery nie-gwiazdki.
                    if(i == dlugosc_wzorzec-1)                     //jeśli wszystkie kolejne litery to gwiazdki lub i to ostatnia litera oraz * zwraca TRUE.
                        return true;
                    i++;
                }    
                while(j < dlugosc_lancuch){                                                    // szukanie najbliższej j w łancuchu zgodnej z i we wzorcu 
                    if((wzorzec[i]==lancuch[j] || wzorzec[i]=='?') && i!=dlugosc_wzorzec-1){
                        int tmp_i =i;                                                          // gdy znalezlismy j sprawdzamy czy kolejne litery do końca wyrazu lub kolejnej *
                        int tmp_j =j;                                                          // do siebie pasują
                        bool wsk = true;
                        while(tmp_j<dlugosc_lancuch && tmp_i<dlugosc_wzorzec && wzorzec[tmp_i]!='*'){
                            if(wzorzec[tmp_i]!=lancuch[tmp_j] && wzorzec[tmp_i]!='?')
                                wsk = false;
                            tmp_i++;
                            tmp_j++;    
                        }
                        if(wsk){i = tmp_i-1; j = tmp_j-1; break;}          //jeśli wszystko ok kontynuujemy główną pętle
                        else{j++; continue;}                       //jeśli nie, szukamy kolejnego pasującego j
                    }    
                    else if(i == dlugosc_wzorzec-1 && j == dlugosc_lancuch-1 && (wzorzec[i]==lancuch[j] || wzorzec[i]=='?'))    // przypadki gdy i nie jest *
                        return true;
                    else if(i == dlugosc_wzorzec-1 && j == dlugosc_lancuch-1 && wzorzec[i]!=lancuch[j] && wzorzec[i]!='?')      // oraz jednoczesnie i jest ostatnią litera wzorca
                        return false;
                    else if(j==dlugosc_lancuch-1 && wzorzec[i]!=lancuch[j])     // gdy nie istnieje odpowiednie j zwracamy FALSE
                        return false;                                                    
                    j++;    
                }
            }

            if(wzorzec[i]!=lancuch[j] && wzorzec[i]!='?' && wzorzec[i]!='*') // gdy i nie jest * lub ? porównujemy z j
                return false;
            j++;    
        }
        return true;
    }

    else //gdy dlugosć wzorca jest równa 0
    {
        if(dlugosc_wzorzec==dlugosc_lancuch)
            return true;
        else
            return false;    
    }      
}    

int main(void)
{
    char tekst1[100]; 
    char tekst2[100];
    printf("Podaj tekst1:\n");
    scanf("%s",tekst1);
    printf("Podaj tekst2:\n");
    scanf("%s",tekst2);
    printf("dlugosc tekstu1 = %ld \ndlugosc tekstu2 = %ld\n", strlen(tekst1), strlen(tekst2));

    if(match(tekst1,tekst2)){
        printf("TAK, tekst spełnia warunki funkcji match.\n");
    }
    else{
        printf("NIE, tekst nie spełnia warunków funkcji match.\n");
    }    
    
    return 0;
}