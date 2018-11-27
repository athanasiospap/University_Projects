#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>

int interactive() {
    char line[513];      //max 512 chars per line
    char *comms[120];    //two dimension array for the number of commands
    char *option[3];     //options ex. ls -l
    int k, i, j, l, exe, status;
    pid_t pid[120];      //same dimension with num of commands
    
    option[2]=NULL;      //Last value of option is NULL for execvp


    for (;;){
        printf("papazoglou_8324> ");    //command prompt
        fgets(line,513,stdin);          //read the command
        if (line == NULL){              
            printf("Error: I had NULL line it's time to exit\n");
            exit(0);
        }
        
        k = 0; //start of command
        comms[k]=strtok(line,";");
        while(comms[k] != NULL){
            //compute the legth of the command but not the terminating NULL char
            i = strlen(comms[k])-1;
            //search for NULL & spaces with shift from the end and replace them with \0
            while (i>=0){
                if (comms[k][i] != '\n' && comms[k][i] != ' '){
                    break;
                }
                else{
                    comms[k][i] = '\0';
                }
                i--;
            }
            //delete spaces (' ') with shift from the beginning
            while(comms[k][0] == ' '){
                j=0;
                while(j<strlen(comms[k])){
                    comms[k][j]=comms[k][j+1];
                    j++;
                }
            }
            k++;
            comms[k] = strtok(NULL,";");
        }

        for (l=0;l<k;l++){
            /* In fork() mother gives something positive
               but the child gives zero 0
               Each fork() is for every command
            */
            pid[l]=fork();
            if (pid[l]<0){
                printf("Error with the fork process\n");
            }
            else if(pid[l]==0){
                //creation of child pid to execute the shell command
                option[0]=strtok(comms[l]," ");    //strtok for first option on current command
                i=1;
                while(option[i] != NULL){
                    option[i]=strtok(NULL," ");
                    i++;
                }
                exe = execvp(option[0],option);  //execvp(current command,whole array)
                if (exe == -1) {
                    printf("Error : wrong command\n");
                }
                exit(0);
            }
        }
        //Parent process, wait for the every child to complete
        for (l=0;l<k;l++){
            waitpid(pid[l],&status,0);
        }

        for (l=0;l<k;l++){
            //Compare the commands for quit
            if(strcmp(comms[l],"quit\0")==0){
                exit(2);
            }
        }
    }
}
