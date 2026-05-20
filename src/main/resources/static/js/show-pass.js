document.addEventListener('DOMContentLoaded', () => {

    console.log('carico');


    const pass = document.getElementById('password')

    const eyeOpen = document.getElementById('eyeIconOpen')

    const eyeClose = document.getElementById('eyeIconClosed')

    const passBtn = document.getElementById('togglePasswordBtn')

    pass.type = 'password'

    passBtn.addEventListener('click', () => {

        console.log('clicko');


        if (pass.type === 'password') {

            pass.type = 'text'
            eyeOpen.classList.add('d-none')
            eyeClose.classList.remove('d-none')

        } else {

            pass.type = 'password'
            eyeOpen.classList.remove('d-none')
            eyeClose.classList.add('d-none')

        }

    })

})