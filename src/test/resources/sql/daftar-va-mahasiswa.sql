select d.nomor_debitur, d.nama, t.status_pembayaran, v.va_status, t.status_notifikasi from
virtual_account v inner join tagihan t
on v.id_tagihan = t.id
inner join debitur d
on t.id_debitur = d.id
where char_length(d.nomor_debitur) < 10
and v.va_status = 'AKTIF'

