describe('Using Tracel parser',function(){
	
	var rootId = 'testContainer';
	
	//Create an easily-removed container for our tests to play in
	beforeEach(function() {
	});
	
	//Clean it up after each spec
	afterEach(function() {
	});
		
	
	
	describe('A property path ',function() {
			
		it('Must always begin with an identifier like myVar',function(){
			expect(tracel.parsePropertyPath("myVar")).toEqual(['myVar']);
		});
		it('can be a sequence of properties like a.b',function(){
			expect(tracel.parsePropertyPath("a.b")).toEqual(['a','b']);
		});

		it('can be like a["b"].c',function(){
			expect(tracel.parsePropertyPath('a["b"].c')).toEqual(['a','b','c']);
		});

		it("can be like a['b'].c",function(){
			expect(tracel.parsePropertyPath("a['b'].c")).toEqual(['a','b','c']);
		});

		
		it('can be like a[b][c]',function(){
			expect(tracel.parsePropertyPath("a[b][c]")).toEqual(['a','b','c']);
		});
		
		it('can be like a["b c"].d',function(){
			expect(tracel.parsePropertyPath('a["b c"].d')).toEqual(['a','b c', 'd']);
		});
		
		it('can be like a[3]',function(){
			expect(tracel.parsePropertyPath("a[3]")).toEqual(['a','3']);
		});
		
		it('can be like b["3"]',function(){
			expect(tracel.parsePropertyPath('b["3"]')).toEqual(['b','3']);
		});
		
		it('can be like c["3.4"]',function(){
			expect(tracel.parsePropertyPath('c["3.4"]')).toEqual(['c','3.4']);
		});
				
		
		it('can\'t begin with a non identifier  like 4b',function(){
			expect(function(){tracel.parsePropertyPath("4b")})  // 
				.toThrowError();
		});
/*		
		it('can\'t be like a["b"].c',function(){			
			expect(tracel.parsePropertyPath("a['b'].c")).toEqual(['a','b','c']);
		});
	*/	
	});		

});