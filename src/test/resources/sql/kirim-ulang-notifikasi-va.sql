update tagihan set status_notifikasi = 'BELUM_TERKIRIM'
where id in (select t.id from
virtual_account v inner join tagihan t
on v.id_tagihan = t.id
inner join debitur d
on t.id_debitur = d.id
where char_length(d.nomor_debitur) < 10
and v.va_status = 'AKTIF');