;IF FIRST NUMBER IS MUCH LESS THAN THE SECOND ADD ZEROS IN THE BEGINNING OF
;FIRST NUMBER TO AVOID ERRORS

section .text

input:
mov eax,3
mov ebx,1
int 0x80
ret

output:
mov eax,4
mov ebx,1
int 0x80
ret

global _start
_start:

mov ecx,inpmsg
mov edx,linp
call output

mov esi,0

_input1:
mov ecx,data
mov edx,1
call input

mov cl,byte[data]


sub cl,30h
mov byte[digit1+esi],cl
add esi,1

cmp cl,-38
je stop1

jmp _input1

stop1:
sub esi,1
mov byte[digit1+esi],10
sub esi,1
mov dword[n1],esi
mov esi,0


mov ecx,inpmsg
mov edx,linp
call output

mov esi,0

_input:
mov ecx,data
mov edx,1
call input

mov cl,byte[data]


sub cl,30h
mov byte[digit2+esi],cl
add esi,1

cmp cl,-38
je proceed

jmp _input

proceed:
sub esi,1
mov byte[digit2+esi],10
sub esi,1
mov dword[n2],esi


mov ebp,dword[n1]

cmp ebp,esi
jg dig
mov dword[n3],esi
jmp checked
dig:
mov dword[n3],ebp
checked:
mov ecx,dword[n3]
add ecx,1
mov byte[digit3+ecx+1],10


_sub:
cmp ebp,0
jnle zeroal
mov byte[digit1+ebp-1],0
zeroal:
mov al,[digit1+ebp]
cmp esi,0
jnle zerobl
mov byte[digit2+esi-1],0
zerobl:
mov bl,[digit2+esi]
mov dl,byte[borrow]
sub al,bl
sub al,dl
mov byte[borrow],0
cmp al,-1
jnle _storing
mov byte[borrow],1
add al,10
_storing:

mov byte[digit3+ecx],al
sub esi,1
sub ebp,1
sub ecx,1
cmp ecx,0
jne _sub

mov dl,byte[borrow]
cmp dl,0
je _printing

mov byte[borrow],0
mov esi,dword[n3]
mov byte[digit3+esi+2],10

mov byte[digit1+0],1
mov ebp,0
compliment:
mov byte[digit1+ebp+1],0
add ebp,1
cmp ebp,esi
jle compliment

mov byte[digit1+ebp+1],10

add esi,1


_subc:
mov al,[digit1+ebp]
mov bl,[digit3+esi]
mov dl,byte[borrow]
sub al,bl
sub al,dl
mov byte[borrow],0
cmp al,-1
jnle _storingc
mov byte[borrow],1
add al,10
_storingc:

mov byte[digit3+ebp],al
sub ebp,1
sub esi,1
cmp ebp,-1
jne _subc

mov byte[digit3],-3


mov esi,0
mov ebp,0

_printing:
mov esi,dword[n3]
cmp esi,50
jle _underlining
mov esi,50
_underlining:
mov ecx,underscore
mov edx,1
call output
sub esi,1
cmp esi,-1
jne _underlining

mov ecx,newline
mov edx,1
call output

mov ecx,newline
mov edx,1
call output

mov ecx,omsg
mov edx,lomsg
call output

mov esi,0

mov ebp,3
spacing:
mov ecx,space
mov edx,1
call output
sub ebp,1
cmp ebp,-1
jne spacing

_output:
mov eax,0
mov al,byte[digit3+esi]
cmp al,10
je stopoutput
add al,30h
mov byte[data],al
mov ecx,data
mov edx,1
call output

add esi,1
jmp _output

stopoutput:

mov ecx,newline
mov edx,1
call output

mov eax,1
mov ebx,0
int 0x80

section .data
omsg db "RESULT = ",10
lomsg equ $-omsg
borrow db 0
n1 dd 0
n2 dd 0
n3 dd 0
newline db 10
data db 30h
underscore db 95
space db 32
inpmsg db "enter the digit",10
linp equ $-inpmsg


section .bss
digit1 resb 4097
digit2 resb 4097
digit3 resb 4098
