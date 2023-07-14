#include <stdio.h> 
#include <stdlib.h>
#include <stdbool.h>
#include <assert.h>

struct element{
    int p[4];
    bool status;
};
void pytanie(int* x,int* y,struct element a){
    printf("[%d] [%d] [%d] [%d] ?\n",a.p[0], a.p[1], a.p[2], a.p[3]);
    int e,f;
    printf("czerwone:");scanf("%d",&e);
    printf("białe:");scanf("%d",&f);
    *x=e;
    *y=f;
}
bool oszust(struct element b[]){
    const int wszystkie_elementy = 6*6*6*6;
    int i = 0;
    while(i<wszystkie_elementy){
        if(b[i].status==true)
            return false;
        i++;    
    }
    return true;
}
bool porownaj(int czerwone, int biale, struct element baza, struct element wzorzec){
    int licznik_czerwone=0;
    int licznik_biale=0;
    int odrzucony=7;
    
    for(int wsk = 0; wsk<4; wsk++){
        if(baza.p[wsk] == wzorzec.p[wsk]){
            licznik_czerwone++;
            wzorzec.p[wsk] = odrzucony;
            baza.p[wsk] = odrzucony;
        }
    }
    for(int wsk1 = 0; wsk1<4; wsk1++){
        if(baza.p[wsk1] != odrzucony){
            for(int wsk2 = 0; wsk2<4; wsk2++){
                if(wzorzec.p[wsk2] != odrzucony && baza.p[wsk1] == wzorzec.p[wsk2]){
                    licznik_biale++;
                    wzorzec.p[wsk2]=odrzucony;
                    break;
                }
            }
        }    
    }       
    if(licznik_biale!=biale || licznik_czerwone!=czerwone){
        return false;
    }
    return true;
}

int ile_roznych(struct element b){
    int counter=1;
    if(b.p[0]!=b.p[1] && b.p[0]!=b.p[2] && b.p[0]!=b.p[3]){counter++;}
    if(b.p[1]!=b.p[2] && b.p[1]!=b.p[3]){counter++;}
    if(b.p[2]!=b.p[3]){counter++;}
    return counter;
}

int znajdz_pozycje(struct element baza[]){
    int const wszystkie_elementy = 6*6*6*6;
    int i=0;
    int n;
    while(i<wszystkie_elementy){
        n = ile_roznych(baza[i]);
        if(baza[i].status==true && n==4)
            return i;
        i++;    
    }
    i=0;
    while(i<wszystkie_elementy){
        n=ile_roznych(baza[i]);
        if(baza[i].status==true && n==3)
            return i;
        i++;    
    }
    i=0;
    while(i<wszystkie_elementy){
        n=ile_roznych(baza[i]);
        if(baza[i].status==true && n==2)
            return i;
        i++;    
    }
    i=0;
    while(i<wszystkie_elementy){    
        if(baza[i].status==true)
            return i;
        i++;    
    }
}

int main(void){
    
    printf("\nPROGRAM GRAJĄCY W MASTERMIND\n");   //inicjowanie zmiennych
    const int wszystkie_elementy = 6*6*6*6;
    int runda = 1;
    int czerwone,biale;
    struct element glowna_baza[wszystkie_elementy];
    
    struct element baza_pom;
    int pozycja = 0;
    for(int a1=1;a1<=6;a1++){                  //inicjacja wszystkich elementów w tablicy
      glowna_baza[pozycja].p[0] = a1;
      for(int a2=1;a2<=6;a2++){
          glowna_baza[pozycja].p[1] =a2;
          for(int a3=1;a3<=6;a3++){
              glowna_baza[pozycja].p[2]=a3;
              for(int a4=1;a4<=6;a4++){
                glowna_baza[pozycja].p[3]=a4;
                baza_pom = glowna_baza[pozycja];
                glowna_baza[pozycja].status = true;
                pozycja++;
                glowna_baza[pozycja]=baza_pom;
              }
            }
        } 
    }

    while(true){                       //głowna pętla,działą dopóki nie zgadnie/wykryje oszustwa
        printf("\nRUNDA %d\n",runda);
        pozycja = znajdz_pozycje(glowna_baza);
            
        pytanie(&czerwone,&biale,glowna_baza[pozycja]);
        if(czerwone == 4){printf("\nmastermind.exe: WYGRAŁEM W %d RUNDZIE !!!\n\n",runda);break;}

        for(int curr=0; curr<wszystkie_elementy; curr++){           //eliminacja złych kombinacji
            if(glowna_baza[curr].status == true){
                if(porownaj(czerwone, biale, glowna_baza[curr], glowna_baza[pozycja]) == false){
                    glowna_baza[curr].status = false;
                }
            }
        }
        if(oszust(glowna_baza)){printf("\nmastermind.exe: OSZUKUJESZ !!!\n\n"); break;}    //sprawdzanie czy użytkownik nie oszukuje
        
        runda++;
    }
    return 0;
}