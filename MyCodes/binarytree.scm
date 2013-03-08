;Copyright (C) 2010 Massachusetts Institute of Technology
;This is free software; see the source for copying conditions. There is NO
;warranty; not even for MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

;Image saved on Tuesday March 9, 2010 at 6:41:34 PM
 ; Release 9.0.1 || Microcode 15.1 || Runtime 15.7 || SF 4.41 || LIAR/i386 4.118
  ;Edwin 3.116
;You are in an interaction window of the Edwin editor.
;Type `C-h' for help, or `C-h t' for a tutorial.
;`C-h m' will describe some commands.
;`C-h' means: hold down the Ctrl key and type `h'.
;Package: (user)

(define (bstc l) (if (null? l) '() (list (car l) (bstc (lesser (cdr l) (car l))) (bstc (greater (cdr l) (car l))))))    ; FUNCTION FOR CREATING BINARY TREE
;Value: bstc



(define (greater l a) (if (null? l) '() (if (>= (car l) a) (cons (car l) (greater (cdr l) a)) (greater (cdr l) a))))   ; RETURNS ALL THE GREATER ELEMENTS IN LIST
;Value: greater




(define (lesser l a) (if (null? l) '() (if (< (car l) a) (cons (car l) (lesser (cdr l) a)) (lesser (cdr l) a))))   ; RETURNS ALL SMALLER ELEMENTS IN THE LIST
;Value: lesser



(bstc '(5 2 6))
;Value 14: (5 (2) (6))

(define (bstsearch l a) (if (null? l) #f (if (= (car l) a) #t (if (< a (car l)) (bstsearch (cadr l) a) (bstsearch (cddr l) a)))))  ; FUNCTION FOR SEARCHING..
;Value: bstsearch

(bstsearch (bstc '(5 23 34)) 3)
;Value: #f
