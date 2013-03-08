;TO COMPILE THIS NASM PROGRAM

;in 32 bit processors

;nasm -f  elf <filename.asm>

;gcc -s -o <filename> <filename.o>

;./<filename>

;in 64 bit processors

;change second line as gcc -m32 -s -o <filename> <filename.o>



section .text

global main

extern printf
extern scanf

print:
mov eax,4
mov ebx,1
int 0x80
ret


printfloat:
push ebp
mov ebp,esp

push dword[data+4]
push dword[data]

push prinfformat
call printf

mov esp,ebp
pop ebp
ret


readfloat:
push ebp
mov ebp,esp

push data
push scanfformat
call scanf

mov esp,ebp
pop ebp
ret

main:
mov ecx,inputn
mov edx,linputn
call print


push n
push scanfint
call scanf

sub dword[n],1

mov ecx,inputx
mov edx,linputx
call print

call readfloat
fld qword[data]

fstp qword[x]
mov eax,dword[n]

cmp eax,0
je done


looping:

fld qword[x]
cmp eax,1
jle adding

mov ebx,eax

powering:
fmul qword[x]
dec ebx
cmp ebx,1
jnle powering
fstp qword[poweredx]

mov ebx,eax
mov eax,1
factorial:
imul ebx
dec ebx
cmp ebx,1
jnle factorial

mov dword[fact],eax

fld qword[poweredx]
fidiv dword[fact]

adding:
fadd qword[sigma]
fstp qword[sigma]



mov eax,dword[n]
sub eax,1
mov dword[n],eax
cmp eax,0
jnle looping

done:

fld qword[sigma]
mov dword[one],1
fiadd dword[one]
fstp qword[data]


call printfloat






exit:
mov eax,1
mov ebx,0
int 0x80

section .bss
data resq 1
fact resd 1
x resq 1
poweredx resq 1
n resd 1
temp resw 1
sigma resq 1
one resd 1
garbage resb 1


section .data
inputx db "INPUT VALUE OF X IN THE EXPRESSION e^x ",10
linputx equ $-inputx
inputn db "INPUT VALUE OF N (number of terms) ",10
linputn equ $-inputn
scanfformat db "%lf",0
scanfint db "%d",0
prinfformat db "answer=%f",10,0
