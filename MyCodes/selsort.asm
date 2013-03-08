section .text

startnew:

mov ecx,newline
mov edx,1
mov eax,4
mov ebx,1
int 0x80
ret

convert:
mov ecx,[inp]
mov bl,10
mov al,cl
sub al,30h

cmp ch,10
je converted
mul bl
sub ch,30h
add al,ch
converted:
mov byte[inp],al
ret

takeinput:
mov ecx,msgele
mov edx,lmsgele
mov eax,4
mov ebx,1
int 0x80

tinput:

mov ecx,inp
mov edx,3
mov eax,3
mov ebx,0
int 0x80


call convert

ret

global _start
_start:

mov ecx,nomsg
mov edx,lnom
mov eax,4
mov ebx,1
int 0x80

call tinput

mov eax,0
mov al,byte[inp]
mov byte[n],al
mov esi,0

taking:
push ax

call takeinput

mov al,byte[inp]
mov byte[input+esi],al

add esi,1
mov eax,0
pop ax
cmp eax,esi
jne taking

mov esi,0
mov eax,0
mov edx,0
mov ecx,0
sorting:
mov edx,esi
mov al,byte[input+edx]
mov byte[temp1],al
comparing:
add edx,1
mov bl,byte[n]
cmp dl,bl
je aftercompare
mov cl,byte[input+edx]
cmp al,cl
jle comparing
mov byte[temp2],dl
mov al,cl
jmp comparing
aftercompare:
cmp al,[temp1]
je noswap
mov byte[input+esi],al
mov ecx,0
mov edx,0
mov cl,[temp1]
mov dl,[temp2]
mov byte[input+edx],cl
noswap:
add esi,1
mov ebx,0
mov bl,byte[n]
cmp esi,ebx
jne sorting



mov ecx,sortmsg
mov edx,lsortmsg
mov eax,4
mov ebx,1
int 0x80

mov ebp,0
movzx ebp,byte[n]

mov esi,0

call startnew

output:
mov eax,0
mov al,byte[input+esi]
push 29h

break:
mov bx,10
mov edx,0
div bx
add dx,30h
push dx
cmp al,0
jne break

mov eax,0
pop ax
print:
mov byte[printdata],al

mov ecx,printdata
mov edx,1
mov eax,4
mov ebx,1
int 0x80

pop ax
cmp al,29h
jne print

mov ecx,space
mov edx,1
mov eax,4
mov ebx,1
int 0x80

add esi,1
cmp ebp,esi
jne output

mov eax,1
mov ebx,0
int 0x80

section .bss
input resb 100


section .data
temp1 db 30h
temp2 db 30h
printdata db 30h
inp dd 30h
space db 32
newline db 10
n db 30h
msgele db "ENTER ELEMENT",32
lmsgele equ $-msgele
nomsg db "ENTER NO: OF ELEMENTS ",32
lnom equ $-nomsg
sortmsg db "SORTED ARRAY",10,7
lsortmsg equ $-sortmsg
