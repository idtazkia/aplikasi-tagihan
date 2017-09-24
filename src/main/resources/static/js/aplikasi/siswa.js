var daftarSiswa = new Vue({
    el: '#daftarSiswa',
    data: {
        dataSiswa : null,
        sudahDicari : false,
        searchSiswa: ""
    },
    methods: {
        cariSiswa: function () {
            var vm = this;
            vm.dataSiswa = null;
            vm.sudahDicari = false;
            console.log("Mencari "+this.searchSiswa);
            axios.get('/api/siswa/', {
                params: {
                    search: vm.searchSiswa
                }
            })
            .then(function (response) {
                console.log(response);
                console.log("Response Data ");
                vm.dataSiswa = response.data;
                console.log(vm.dataSiswa);
                vm.sudahDicari = true;

            })
            .catch(function (error) {
                console.log(error);
                vm.sudahDicari = true;
            });
        }
    }
})