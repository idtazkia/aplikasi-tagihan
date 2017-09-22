# Manajemen Tagihan Uang Sekolah dan Virtual Account #

Aplikasi ini berguna untuk mengelola tagihan uang sekolah dan pembayarannya melalui Virtual Account Bank.

## Daftar Fitur ##

* Import Data Siswa
* Konfigurasi Jenis Tagihan, misalnya

    * Uang Pangkal
    * Uang Gedung
    * SPP
    * Kegiatan Sekolah
    * Wisuda
    * dan sebagainya

* Pembuatan Tagihan

    * Menggunakan API (agar bisa integrasi dengan aplikasi akademik)
    * Bulk Import CSV
    * Entri Manual

* Create Virtual Account di Bank yang sudah bekerja sama. Saat ini bank yang didukung :

    * BNI / BNI Syariah

* Pembayaran

    * Melalui Virtual Account
    * Melalui Transfer Bank (dengan kode unik)

* Laporan

    * Tagihan Outstanding per Siswa
    * Histori Pembayaran per Siswa
    * Rekap Tagihan per Status (chart)

## Teknologi, Framework, Tools, Platform ##

Untuk bisa melakukan build dan menjalankan aplikasi, ada beberapa software yang harus dipasang, diantaranya:

* Java 8
* Maven 3.3.9
* PostgreSQL 9.5 
* Spring Boot 1.5.6
* Heroku

## Lisensi ##

Aplikasi ini dirilis secara open-source dengan lisensi Apache License versi 2.0, yang [secara garis besar artinya sebagai berikut](https://tldrlegal.com/license/apache-license-2.0-(apache-2.0)):

* Anda wajib :

    * mencantumkan pemilik hak cipta aslinya (yaitu kami para penulisnya)
    * menyertakan lisensi Apache License versi 2.0 setiap kali membagikan aplikasi ini
    * menjelaskan perubahan yang dilakukan terhadap aplikasi ini

* Anda boleh :

    * menggunakannya untuk keperluan komersil/bisnis
    * membagikannya kepada siapa saja
    * memodifikasi sesuai kebutuhan
    * menyimpan modifikasinya untuk diri sendiri (tidak harus dirilis open-source juga)

* Anda tidak bisa :

    * menyalahkan pembuatnya (yaitu kami) bila terjadi kerusakan/kerugian apapun
    * menggunakan merek dagang pembuatnya (yaitu kami) untuk mempromosikan kegiatan Anda

## Kontribusi ##

Kami mengharapkan kontribusi dari rekan-rekan dalam berbagai bentuk, diantaranya:

* ikut serta coding
* menggunakan, mengetes, dan melaporkan kalau ada bug/error/usulan perbaikan
* membuat dokumentasi
* dan bantuan lain dalam bentuk apapun

Bila rekan-rekan ingin berkontribusi, caranya sangat mudah.

* Kontribusi menulis kode program : [fork repo ini](https://github.com/idtazkia/payment-virtualaccount#fork-destination-box), lakukan apa yang ingin Anda lakukan, kemudian submit Pull Request (PR). Kami akan review dan merge PR yang berkualitas bagus. Untuk lebih jelasnya bisa nonton [video tutorial tentang workflow Pull Request ini](https://www.youtube.com/watch?v=gDqT_Wvt3VQ)

* Melaporkan bug/error/usulan : cukup [buat issue baru](https://github.com/idtazkia/payment-virtualaccount/issues/new).

* Membuat dokumentasi : caranya sama dengan kontribusi kode program. Fork, tulis, submit PR.

* Bantuan lain : silahkan japri ke [endy@tazkia.ac.id](mailto:endy@tazkia.ac.id)
