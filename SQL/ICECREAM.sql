drop table iorder;
drop table member;
drop table product;

CREATE TABLE  MEMBER (
                                IDX NUMBER(6) CONSTRAINT MEMBER_IDX_PK PRIMARY KEY,
                                ID VARCHAR2(20) CONSTRAINT MEMBER_ID_NN NOT NULL,
                                PW VARCHAR2(20)CONSTRAINT MEMBER_PW_NN NOT NULL,
                                NAME  VARCHAR2(20)CONSTRAINT MEMBER_NAME_NN NOT NULL ,
                                PHONENUM  VARCHAR2(20)CONSTRAINT MEMBER_NUM_NN NOT NULL,
                                EMAIL VARCHAR2(50)CONSTRAINT MEMBER_EMAIL_NN NOT NULL
                              ) ;
          
CREATE TABLE  PRODUCT (
                                ICODE NUMBER(6) CONSTRAINT PRODUCT_ICODE_PK PRIMARY KEY,
                                 
                                INAME  VARCHAR2(50)CONSTRAINT PRODUCT_NAME_NN NOT NULL ,
                                IPRICE  INTEGER  ,
                                COUNT INTEGER
                               
                              ) ;          
          
          
          
                              
                              
CREATE TABLE IORDER (           oidx NUMBER(6) CONSTRAINT ORDER_OCODE_PK PRIMARY KEY,
                                ORDERCODE integer ,
                                ICODE NUMBER(6) CONSTRAINT ORDER_ICODE_FK REFERENCES PRODUCT(ICODE) NOT NULL,
                                IDX NUMBER(6) CONSTRAINT ORDER_IDX_FK REFERENCES MEMBER(IDX) NOT NULL ,
                                ORDERDATE  DATE DEFAULT SYSDATE,
                                count Integer,
                                oprice Integer
                               
                              ) ;
--시퀀스 삭제
drop sequence member_idx_seq ;
drop sequence iorder_oidx_seq ;


--시퀀스 생성
create sequence member_idx_seq ;
create sequence iorder_oidx_seq ;
                              


--product dml --- icode, iname, iprice, count
insert into product values (1,'바닐라 아이스크림', '2000',100);
insert into product values (2,'초코 아이스크림', '2500',100);
insert into product values (3,'딸기 아이스크림', '2500',100);
insert into product values (4,'바나나 아이스크림', '2700',100);
insert into product values (5,'커피 아이스크림', '2700',100);
insert into product values (6,'민트 아이스크림', '2800',100);
insert into product values (7,'요거트 아이스크림', '3000',100);


commit;


--member dml  --idx ,id,pw, name, phonenum, email
insert into member values (member_idx_seq.nextval, 'apple','1234','홍길동', '01012341234','fods@gmail.com');
insert into member values (member_idx_seq.nextval, 'banana','1234','강호동', '01045741234','djwd@gmail.com');



--iorder dml -- oidx, ordercode, icode,idx,orderdate,count,price
insert into iorder values (iorder_oidx_seq.nextval, '', 1,member_idx_seq.currval,sysdate,5,10000);
insert into iorder values (iorder_oidx_seq.nextval, '', 1,1,'21/06/22',5,10000);
insert into iorder values (iorder_oidx_seq.nextval, '', 1,2,'21/05/22',5,10000);



              
update product set count=5 where icode = 1;
              
select * from iorder;
select * from member;
select * from product;






update product set count =count-(select count from iorder where oidx = 1)where icode=1;



