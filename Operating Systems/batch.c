#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>

int batch(char path[]){
    FILE *batch;
    char line[513];
    char *comms[120]; //two dimension array for the number of commands
    char *com[3];
    int k, i, j, l, exe, childstatus;
    pid_t pid[120];
    com[2]=NULL;

    batch = fopen(path,"r");
    if (batch == NULL){
        printf("Error : can't open file\n");
        exit(0);
    }
    if (fgets(line,513,batch)==NULL) {
        exit(1);
    }
    k = 0; //start of command
    comms[k]=strtok(line,";");
    while(feof(batch)==0){
        while(comms[k] != NULL){
            if (feof(batch)){
                   exit(2);
            }
            //compute the legth of the command but not the terminating NULL char
            i = strlen(comms[k])-1;
            //search for NULL & spaces with shift and replace them with \0
            while (i>=0){
                if (comms[k][i] != '\n' && comms[k][i]!=' '){
                    break;
                }
                else{
                    comms[k][i] = '\0';
                }
                i--;
            }
            //delete spaces' ' with shift from the beginning
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
            /* In fork() mother gives somthing positive
            but the child gives zero 0
            */
            pid[l]=fork();
            if (pid[l]<0){
                printf("Error with the fork process\n");
            }
            else if(pid[l]==0){
                //creation of child pid
                com[0]=strtok(comms[l]," ");
                i=1;
                while(com[i] != NULL){
                    com[i]=strtok(NULL," ");
                    i++;
                }
                exe = execvp(com[0],com);  //execvp(current command,whole array)
                if (exe == -1) {
                    printf("Error : wrong command\n");
                }
                exit(0);
            }
        }
        for (l=0;l<k;l++){
            waitpid(pid[l],&childstatus,0);
        }

        for (l=0;l<k;l++){
            if(strcmp(comms[l],"quit\0")==0){
                exit(3);
            }
        }
    }
    fclose(batch);
}
