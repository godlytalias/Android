section .text

global main

extern printf,scanf

print:
mov eax,4
mov ebx,1
int 0x80
ret

main:

mov ecx,inputa
mov edx,linputa
call print

push a
push scan
call scanf

mov ecx,inputb
mov edx,linputb
call print

push b
push scan
call scanf

mov ecx,inputc
mov edx,linputc
call print

push c
push scan
call scanf

fld qword[b]
fmul st0
fld qword[a]
fmul qword[c]
mov word[const],4
fimul word[const]
fchs
fadd st1
fst qword[disc]

push dword[disc+4]
push dword[disc]
push dis
call printf

ftst
fstsw ax
sahf
ja real_roots
sahf
je one_root

imag_roots:
fchs
fsqrt
fld qword[b]
fchs
fadd st1
fld qword[a]
mov word[const],2
fimul word[const]
fxch st1
fdiv st1
fstp qword[x1]
fld qword[disc]
fchs
fsqrt
fld qword[b]
fadd st1
fchs
fld qword[a]
mov word[const],2
fimul word[const]
fxch st1
fdiv st1
fstp qword[x2]

push dword[x2+4]
push dword[x2]
push dword[x1+4]
push dword[x1]
push imagroot
call printf
jmp over

real_roots:
fsqrt
fld qword[b]
fchs
fadd st1
fld qword[a]
mov word[const],2
fimul word[const]
fxch st1
fdiv st1
fstp qword[x1]
fld qword[disc]
fsqrt
fld qword[b]
fadd st1
fchs
fld qword[a]
mov word[const],2
fimul word[const]
fxch st1
fdiv st1
fstp qword[x2]

push dword[x2+4]
push dword[x2]
push dword[x1+4]
push dword[x1]
push realroot
call printf

jmp over

one_root:
fsqrt
fld qword[b]
fchs
fadd st1
fld qword[a]
mov word[const],2
fimul word[const]
fxch st1
fdiv st1
fstp qword[x1]


push dword[x1+4]
push dword[x1]
push oneroot
call printf


over:
mov eax,1
mov ebx,0
int 0x80

section .bss
x1 resq 1
x2 resq 1
const resw 1
a resq 1
b resq 1
c resq 1
disc resq 1

section .data
scan db "%lf",0
oneroot db "Root = %f",10,0
realroot db "Root 1 = %f    &   Root 2 = %f ",10,0
imagroot db "Root 1 = %fi   &   Root 2 = %fi ",10,0


inputa db "INPUT THE CO-EFFICIENT a ( a(x^2) + bx + c ) ",10
linputa equ $-inputa
inputb db "INPUT THE CO-EFFICIENT b ( a(x^2) + bx + c ) ",10
linputb equ $-inputb
inputc db "INPUT THE CO-EFFICIENT c ( a(x^2) + bx + c ) ",10
linputc equ $-inputc
dis db "DISCRIMINANT  =  %f ",10,0
