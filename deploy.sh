#!/bin/bash

if [[ $# -eq 0 ]] ; then
    echo 'Cara pakai : deploy.sh <namafile.jar>'
    exit 1
fi

service aplikasi-tagihan stop
rm /var/lib/aplikasi-tagihan/aplikasi-tagihan.jar
ln -s /var/lib/aplikasi-tagihan/$1 /var/lib/aplikasi-tagihan/aplikasi-tagihan.jar
service aplikasi-tagihan start