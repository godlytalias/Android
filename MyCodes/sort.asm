;ENTER YOUR INPUTS IN 4 BIT FORMAT(eg. 0030)

section .text

input:
mov eax,3
mov ebx,0
int 0x80
ret

output:
mov eax,4
mov ebx,1
int 0x80
ret

convert2d:
sub cl,30h
sub ch,30h
mov eax,0
mov al,cl
mov bl,10
mul bl
add al,ch
ret

convertbin:
call convert2d
shr ecx,16
sub cl,30h
sub ch,30h
mov bx,10
mul bl
add al,cl
mul bx
mov dx,0
mov dl,ch
add ax,dx
ret



global _start
_start:

mov ecx,inpno
mov edx,linpno
call output

mov ecx,num
mov edx,2
call input

mov ecx,temp
mov edx,1
call input

mov ecx,0
mov cx,word[num]
call convert2d
mov byte[no],al
mov byte[noi],al

taking:
mov ecx,inpmsg
mov edx,linp
call output

mov ecx,num
mov edx,4
call input

mov ecx,temp
mov edx,1
call input

mov ecx,dword[num]
call convertbin
mov edx,0
sub byte[noi],1
mov dl,byte[noi]
mov word[binary+2*edx],ax
cmp dx,0
jne taking

mov ebp,0

sort:
mov esi,0
insort:
mov ax,word[binary+2*esi]
mov bx,word[binary+2*esi+2]
cmp ax,bx
jle continue
mov word[binary+2*esi],bx
mov word[binary+2*esi+2],ax
continue:
add esi,1
mov edx,0
mov dl,byte[no]
sub dl,1
cmp esi,edx
jne insort
add ebp,1
mov edx,0
mov dl,byte[no]
cmp ebp,edx
jne sort

printoutput:
mov ebp,0
mov ecx,entered
mov edx,1
call output

mov ecx,omsg
mov edx,lomsg
call output
break:
push 29h
mov ax,word[binary+2*ebp]
breaking:
mov bx,10
mov edx,0
div bx
add dl,30h
push dx
cmp ax,0
jne breaking

print:
mov ax,0
pop ax
printing:
mov byte[printdata],al

mov ecx,printdata
mov edx,1
call output

pop ax
cmp ax,29h
jne printing

mov ecx,space
mov edx,1
call output

add ebp,1
mov edx,0
mov dl,byte[no]
cmp edx,ebp
jne break

mov ecx,entered
mov edx,1
call output

mov eax,1
mov ebx,0
int 0x80

section .bss
binary resw 100

section .data
printdata db 0
temp db  0
entered db 10
space db 32
no db 0
noi db 0
num dd 0
inpno db "ENTER THE NUMBER OF INPUTS  : ",32
linpno equ $-inpno
inpmsg db "ENTER THE INPUT(4 BIT BINARY) : ",32
linp equ $-inpmsg
omsg db "SORTED ARRAY : ",32
lomsg equ $-omsg
