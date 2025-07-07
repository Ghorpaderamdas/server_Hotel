'use client'

import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { motion, AnimatePresence } from 'framer-motion'
import { Calendar, Users, Wifi, Car, Coffee, Tv, Bath, Star } from 'lucide-react'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { CardSkeleton } from '@/components/ui/loading-skeleton'
import { roomsApi, type Room } from '@/lib/api'
import { formatCurrency } from '@/lib/utils'
import Link from 'next/link'
import Image from 'next/image'

const roomTypes = ['All', 'Standard', 'Deluxe', 'Suite']

const amenityIcons: Record<string, any> = {
  'WiFi': Wifi,
  'Parking': Car,
  'Coffee': Coffee,
  'TV': Tv,
  'Bathroom': Bath,
  'Air Conditioning': Star,
}

export default function RoomsPage() {
  const [selectedType, setSelectedType] = useState('All')
  const [checkIn, setCheckIn] = useState('')
  const [checkOut, setCheckOut] = useState('')

  const { data: roomsResponse, isLoading } = useQuery({
    queryKey: ['rooms'],
    queryFn: () => roomsApi.getAll(),
  })

  const rooms = roomsResponse?.data?.data || []

  const filteredRooms = rooms.filter((room: Room) => 
    selectedType === 'All' || room.type.toLowerCase() === selectedType.toLowerCase()
  )

  return (
    <div className="min-h-screen pt-16">
      {/* Header */}
      <section className="bg-gradient-to-r from-gray-900 to-gray-800 text-white py-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
            className="text-center"
          >
            <h1 className="text-4xl lg:text-5xl font-serif font-bold mb-6">
              Our Rooms & Suites
            </h1>
            <p className="text-xl text-gray-300 max-w-2xl mx-auto">
              Choose from our collection of luxury accommodations, each designed 
              to provide comfort and stunning mountain views.
            </p>
          </motion.div>
        </div>
      </section>

      {/* Filters */}
      <section className="py-8 bg-white dark:bg-gray-900 border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex flex-col lg:flex-row gap-6 items-center justify-between">
            {/* Room Type Filter */}
            <div className="flex flex-wrap gap-2">
              {roomTypes.map((type) => (
                <Button
                  key={type}
                  variant={selectedType === type ? 'gold' : 'outline'}
                  size="sm"
                  onClick={() => setSelectedType(type)}
                >
                  {type}
                </Button>
              ))}
            </div>

            {/* Date Filters */}
            <div className="flex gap-4">
              <div className="flex items-center gap-2">
                <Calendar className="h-4 w-4 text-gray-500" />
                <Input
                  type="date"
                  placeholder="Check-in"
                  value={checkIn}
                  onChange={(e) => setCheckIn(e.target.value)}
                  className="w-40"
                />
              </div>
              <div className="flex items-center gap-2">
                <Calendar className="h-4 w-4 text-gray-500" />
                <Input
                  type="date"
                  placeholder="Check-out"
                  value={checkOut}
                  onChange={(e) => setCheckOut(e.target.value)}
                  className="w-40"
                />
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Rooms Grid */}
      <section className="py-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          {isLoading ? (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
              {[...Array(6)].map((_, i) => (
                <CardSkeleton key={i} />
              ))}
            </div>
          ) : (
            <AnimatePresence mode="wait">
              <motion.div
                key={selectedType}
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                exit={{ opacity: 0 }}
                transition={{ duration: 0.3 }}
                className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8"
              >
                {filteredRooms.map((room: Room, index: number) => (
                  <motion.div
                    key={room.id}
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.6, delay: index * 0.1 }}
                  >
                    <Card className="overflow-hidden hover:shadow-xl transition-all duration-300 group">
                      <div className="relative h-64 overflow-hidden">
                        <Image
                          src={room.images?.[0] || 'https://images.pexels.com/photos/164595/pexels-photo-164595.jpeg'}
                          alt={room.name}
                          fill
                          className="object-cover group-hover:scale-110 transition-transform duration-500"
                        />
                        <div className="absolute top-4 right-4">
                          <span className="bg-gold-600 text-white px-3 py-1 rounded-full text-sm font-medium">
                            {room.type}
                          </span>
                        </div>
                      </div>
                      
                      <CardHeader>
                        <CardTitle className="flex items-center justify-between">
                          <span>{room.name}</span>
                          <div className="flex items-center gap-1">
                            <Users className="h-4 w-4 text-gray-500" />
                            <span className="text-sm text-gray-500">{room.maxOccupancy}</span>
                          </div>
                        </CardTitle>
                      </CardHeader>
                      
                      <CardContent className="space-y-4">
                        <p className="text-gray-600 dark:text-gray-400 text-sm">
                          {room.description}
                        </p>
                        
                        {/* Amenities */}
                        <div className="flex flex-wrap gap-2">
                          {room.amenities?.slice(0, 4).map((amenity) => {
                            const Icon = amenityIcons[amenity] || Star
                            return (
                              <div
                                key={amenity}
                                className="flex items-center gap-1 bg-gray-100 dark:bg-gray-800 px-2 py-1 rounded-md text-xs"
                              >
                                <Icon className="h-3 w-3" />
                                <span>{amenity}</span>
                              </div>
                            )
                          })}
                        </div>
                        
                        <div className="flex items-center justify-between pt-4">
                          <div>
                            <span className="text-2xl font-bold text-gold-600">
                              {formatCurrency(room.pricePerNight)}
                            </span>
                            <span className="text-gray-500 text-sm">/night</span>
                          </div>
                          <Button variant="gold" asChild>
                            <Link href={`/rooms/${room.id}`}>
                              View Details
                            </Link>
                          </Button>
                        </div>
                      </CardContent>
                    </Card>
                  </motion.div>
                ))}
              </motion.div>
            </AnimatePresence>
          )}
          
          {!isLoading && filteredRooms.length === 0 && (
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              className="text-center py-12"
            >
              <h3 className="text-xl font-semibold mb-2">No rooms found</h3>
              <p className="text-gray-600 dark:text-gray-400">
                Try adjusting your filters or check back later.
              </p>
            </motion.div>
          )}
        </div>
      </section>
    </div>
  )
}