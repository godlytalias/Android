;PROGRAM FOR HUFFMAN CODING
;PROGRAMMER : GODLY T.ALIAS

(define (huffmanencode l fl) (if (null? l) '() (cons (codefreq (car l)(huffmancode fl)) (huffmanencode (cdr  l) fl))))
;changes a list of alphabets into huffmancodes if list of alphabets(l) and frequency list(fl) is passed


(define (huffmandecode l fl) (if (null? l) '() (cons (codereturn (car l) (huffmancode fl)) (huffmandecode (cdr l) fl))))
;changes a list of huffmancodes to alphabets if the list of huffmancodes and frequency list is passed


(define (codereturn hcode l) (if (= (cadar l) hcode) (code l) (codereturn hcode (cdr l))))


(define (listsum l) (if (null? l) 0 (if (list? (car l)) (+ (listsum (car l)) (listsum (cdr l))) (+ (car l) (listsum (cdr l))))))
;sum of elements in a list

(define (search l a) (if (flat? l) (if (list? l) (if (null? l) #f (if (eq? a (car l)) #t (search (cdr l) a))) (if (= l a) #t #f)) (or (search (car l) a) (search (cdr l) a))))

(define (flat l)
(if (null? l) '()
(if (list? (car l)) (append (flat (car l))(flat (cdr l))) (cons (car l) (flat (cdr l))))) )
;flattens a list


(define (sortdesc l m)  (if (null? l) m  (sortdesc (cdr l) (insertsort m (car l)))))

(define (insertsort l a) (if (null? l) (list a) (if (>= a (car l)) (cons a l) (cons (car l) (insertsort (cdr l) a)))))

(define (sort l) (sortdesc l '()))
(define (code l) (caar l))
(define (frequency l) (cadar l))
(define (freqlist l) (if (null? l) '() (cons (frequency l) (freqlist (cdr l)))))
(define (codelist l) (if (null? l) '() (cons (code l) (codelist (cdr l)))))
(define (listend l) (null? (cddr l)))
(define (pairend l) (and (list? (cadr l)) (null? (cddr l))))

(define (freqdesc l) (sort (freqlist l)))


(define (hofpair l) (if (pairend l) l (hofpair (hofpairing l))))
(define (hofpairing l) (cons (car l) (pairing (cdr l))))
(define (pairing l)
(if (listend l)  (list (list (car l) (cadr l))) (if
(< (listsum (list (car l) (cadr l))) (listsum (list (cadr l) (caddr l)))) 
(cons (list (car l) (cadr l)) (cddr l))  (cons (car l) (pairing (cdr l) ) ) ) ) )

(define (hofpairgen l) (hofpair (sort (freqlist l))))

(define (codefreq a l) (if (null? l) "ERROR" (if (eq? (code l) a) (frequency l) (codefreq a (cdr l)))))

(define (huffman_encoder a l) (encode 0 (codefreq a l) (hofpairgen l)))

(define (encode E fr l)
(if (null? (cdr l)) E
(if (list? (car l))
(if (search (flat (car l)) fr)
(encode (+ (* E 10) 0) fr (car l))
(encode (+ (* E 10) 1) fr (cadr l)) )
(if (eq? fr (car l)) (+ (* E 10) 0) (if (list? (cadr l)) (encode (+ (* E 10) 1)  fr (cadr l)) (encode (+ (* E 10) 1) fr (cdr l)))))))

(define (flat? l) (if (list? l) (if (null? l) #t (if (list? (car l)) #f  (flat? (cdr l)))) #t))

(define (modifier l a)
(if (null? l) '()
 (if (list? (car l))
  (if (search (car l) a) (cons (modifier (car l) a) (cdr l))
(if (flat? l) (cons (car l) (modifier (cdr l) a)) (list (car l) (modifier (cadr l) a)))) (if (= (car l) a) (cons -1 (cdr l)) (if (flat? l) (cons (car l) (modifier (cdr l) a)) (list (car l) (modifier (cadr l) a)))))))



(define (huffmancode l)
(define (dupcaller codes l hofpair) (if (null? codes) '() (cons (list (code codes) (encode 0 (codefreq (code codes) l) hofpair)) (dupcaller (cdr codes) l (modifier hofpair (codefreq (code codes) l))))))


(define alphabets '((A 1) (B 2) (C 3) (D 4) (E 5) (F 6) (G 7) (H 8) (I 9) (J 10) (K 11) (L 12) (M 13) (N 14) (O 15) (P 16) (Q 17) (R 18) (S 19) (T 20) (U 21) (V 22) (W 23) (X 24) (Y 25) (Z 26)))
;assigning frequency  to alphabets by default


(define (dupcheck l) (define (dup l) (if (null? (cdr l)) #t (if (= (car l) (cadr l)) #f  (dup (cdr l)))))  (dup (sort (freqlist l))))
(define (hc codes l) (if (null? codes) '() (cons (list (code codes) (huffman_encoder (code codes) l)) (hc (cdr codes) l))))
(if (dupcheck l) (hc l l)  (dupcaller l l (hofpairgen l))))



;CREDITS : GODLY T. ALIAS - HUFFMANCODING
