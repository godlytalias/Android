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


_sum:
cmp ebp,-1
je alzero
jmp alstored
alzero:
add ebp,1
mov byte[digit1+ebp],0
alstored:
mov al,byte[digit1+ebp]
cmp ebp,-1
jle blzero
jmp blstored
blzero:
add esi,1
mov byte[digit2+esi],0
blstored:
mov bl,byte[digit2+esi]
add al,bl
mov bl,byte[carry]
add al,bl
mov byte[carry],0
cmp al,9
jle _storing
sub al,10
mov byte[carry],1
_storing:

mov byte[digit3+ecx],al
sub esi,1
sub ebp,1
sub ecx,1
cmp ecx,-1
jne _sum

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
carry db 0
n1 dd 0
n2 dd 0
n3 dd 0
newline db 10
data db 30h
underscore db 95
space db 32
inpmsg db "enter the digit",10
linp equ $-inpmsg
omsg db "SUM = ",10
lomsg equ $-omsg

section .bss
digit1 resb 4097
digit2 resb 4097
digit3 resb 4098
