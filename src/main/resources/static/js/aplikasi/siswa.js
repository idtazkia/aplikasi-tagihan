var daftarSiswa = new Vue({
    el: '#daftarSiswa',
    data: {
        dataSiswa : null,
        sudahDicari : false,
        searchSiswa: "",
        searchedKeyword: ""
    },
    methods: {
        cariSiswa: _.debounce(function(){
            var vm = this;
            vm.dataSiswa = null;
            vm.sudahDicari = false;

            if(vm.searchSiswa.trim().length < 1){
                vm.dataSiswa = null;
                return;
            }

            axios.get('/api/siswa/', {
                params: {
                    search: vm.searchSiswa
                }
            })
            .then(function (response) {
                vm.dataSiswa = response.data;
                vm.sudahDicari = true;
                vm.searchedKeyword = vm.searchSiswa;

            })
            .catch(function (error) {
                console.log(error);
                vm.sudahDicari = true;
                vm.searchedKeyword = vm.searchSiswa;
            });
        },1000),
        editSiswa: function(siswa){
            console.log(siswa);
            window.location.href = "form?id=" + siswa.id;
        }
    }
});