

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
mov byte[digit2+esi],10
sub esi,1
mov dword[n2],esi


mov ebp,dword[n1]

mov dword[n3],ebp
add dword[n3],esi
add dword[n3],1

mov ecx,dword[n3]
mov byte[digit3+ecx+1],10

mov esi,dword[n1]
mov ebp,dword[n2]
sub esi,1
sub ebp,1

multiplying:
mov al,byte[digit1+esi]
mov bl,byte[digit2+ebp]
mov dl,byte[carry]
mov byte[carry],0
mul bl
add al,dl
cmp al,9
jle store

;finding carry
mov dx,10
mov ah,0
idiv dl
mov byte[carry],al
mov al,ah
mov ah,0

store:

mov bl,byte[scarry]
add al,bl

add byte[digit3+ecx],al


mov byte[scarry],0
mov al,byte[digit3+ecx]
cmp al,9
jle stored

sub byte[digit3+ecx],10
mov byte[scarry],1


stored:

sub ecx,1
sub esi,1
cmp esi,0
jnl multiplying

mov dl,byte[scarry]
mov byte[digit3+ecx],dl
mov dl,byte[carry]
add byte[digit3+ecx],dl

;finding position of the digits to be added to final product
sub ebp,1
mov ecx,dword[n2]
sub ecx,1
sub ecx,ebp
mov ebx,dword[n3]
sub ebx,ecx
mov ecx,ebx
mov esi,dword[n1]
sub esi,1
mov byte[scarry],0
mov byte[carry],0
cmp ebp,0
jnl multiplying



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

zerojumper:
mov eax,0
mov al,byte[digit3+esi]
cmp al,0
jne _output
add esi,1
jmp zerojumper

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
carry db 0                 ;carry in multiplication
n1 dd 0                    ;digits in multiplicand
n2 dd 0                    ;digits in multiplier
n3 dd 0                    ;digits in product
scarry db 0                ;carry during addition of products
newline db 10
data db 30h
underscore db 95
space db 32
inpmsg db "enter the digit",10
linp equ $-inpmsg
omsg db "RESULT = ",10
lomsg equ $-omsg

section .bss
digit1 resb 4097
digit2 resb 4097
digit3 resb 4098
;PROGRAMMED BY GODLY T.ALIAS
